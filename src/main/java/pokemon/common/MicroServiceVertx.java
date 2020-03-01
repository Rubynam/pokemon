package pokemon.common;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MicroServiceVertx extends AbstractVerticle {

  private ServiceDiscovery mDiscovery;
  private Set<Record> mRegisteredRecords = new ConcurrentHashSet<>();
  private Logger logger = LoggerFactory.getLogger(MicroServiceVertx.class);


  @Override
  public void start() throws Exception {
    super.start();
    createServiceDiscovery();

  }

  @Override
  public void stop(Future<Void> stopFuture) throws Exception {
    super.stop(stopFuture);

    List<Future> futures = mRegisteredRecords
      .stream()
      .map(this::unpublish)
      .collect(Collectors.toList());

    if (futures.isEmpty()) {
      stopDiscovery(stopFuture);
    } else {
      stopServices(futures, stopFuture);
    }
  }

  protected void publishHttpEndpoint(String endpoint, String host, int port, Handler<AsyncResult<Void>> completion) throws Exception {
    Record record = HttpEndpoint.createRecord(endpoint, host, port, "/");
    pushlish(record, completion);
  }

  private void createServiceDiscovery() {
    JsonObject object = config();
    ServiceDiscoveryOptions options = new ServiceDiscoveryOptions().setBackendConfiguration(object);
    mDiscovery = ServiceDiscovery.create(vertx, options);
  }

  private void pushlish(Record record, Handler<AsyncResult<Void>> completion) throws Exception {
    if (mDiscovery == null) {
      start();
    }

    mDiscovery.publish(record, args -> {
      if (args.succeeded()) {
        logger.info("pushlish service", args.getClass().getName());
        mRegisteredRecords.add(record);
      }
      completion.handle(args.map((Void) null));
    });
  }

  private Future<Void> unpublish(Record record) {
    Future<Void> unregisteringFuture = Future.future();
    mDiscovery.unpublish(record.getRegistration(), unregisteringFuture);

    return unregisteringFuture;
  }

  private void stopDiscovery(Future<Void> stopFuture) {
    mDiscovery.close();
    stopFuture.setHandler(AsyncResult::result);
  }

  private void stopServices(List<Future> futures, Future<Void> stopFuture) {
    CompositeFuture composite = CompositeFuture.all(futures);
    composite.setHandler(ar -> {
      mDiscovery.close();
      if(ar.failed()){
        logger.info("close service err {}",ar.cause());
        ar.result().complete();
      }else{
        logger.info("close success");
        ar.result().complete();
      }
    });
  }
}
