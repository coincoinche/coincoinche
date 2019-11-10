package com.coincoinche.engine.serialization.json;

import com.coincoinche.engine.MoveBidding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

/** Custom Serializer for MoveBidding objects. */
public class MoveBiddingSerializer extends StdSerializer<MoveBidding> {

  private static final long serialVersionUID = 5578158945272215492L;

  public MoveBiddingSerializer() {
    this(null);
  }

  public MoveBiddingSerializer(Class<MoveBidding> t) {
    super(t);
  }

  @Override
  public void serialize(MoveBidding value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    gen.writeStartObject();
    gen.writeStringField("bid", value.getShortName());
    gen.writeEndObject();
  }
}
