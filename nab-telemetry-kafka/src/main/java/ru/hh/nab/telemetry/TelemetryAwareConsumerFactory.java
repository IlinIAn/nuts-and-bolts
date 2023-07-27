package ru.hh.nab.telemetry;

import io.opentelemetry.api.OpenTelemetry;
import java.util.function.Supplier;
import org.slf4j.Logger;
import ru.hh.nab.kafka.consumer.ConsumeStrategy;
import ru.hh.nab.kafka.consumer.ConsumerGroupId;
import ru.hh.nab.kafka.consumer.DefaultConsumerFactory;
import ru.hh.nab.kafka.consumer.DeserializerSupplier;
import ru.hh.nab.kafka.util.ConfigProvider;
import ru.hh.nab.metrics.StatsDSender;

public class TelemetryAwareConsumerFactory extends DefaultConsumerFactory {
  private final OpenTelemetry telemetry;

  public TelemetryAwareConsumerFactory(
      ConfigProvider configProvider,
      DeserializerSupplier deserializerSupplier,
      StatsDSender statsDSender,
      Logger logger,
      OpenTelemetry telemetry,
      Supplier<String> bootstrapSupplier
  ) {
    super(configProvider, deserializerSupplier, statsDSender, logger, bootstrapSupplier);

    this.telemetry = telemetry;
  }

  public TelemetryAwareConsumerFactory(
      ConfigProvider configProvider,
      DeserializerSupplier deserializerSupplier,
      StatsDSender statsDSender,
      OpenTelemetry telemetry,
      Supplier<String> bootstrapSupplier
  ) {
    super(configProvider, deserializerSupplier, statsDSender, bootstrapSupplier);

    this.telemetry = telemetry;
  }

  @Override
  protected <T> ConsumeStrategy<T> prepare(ConsumerGroupId consumerGroupId, ConsumeStrategy<T> consumeStrategy) {
    return new TelemetryConsumeStrategyWrapper<>(
        configProvider.getKafkaClusterName(), super.prepare(consumerGroupId, consumeStrategy), consumerGroupId, telemetry);
  }
}
