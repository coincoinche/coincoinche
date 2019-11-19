package com.coincoinche.engine.serialization.json;

import com.coincoinche.engine.MoveBidding;
import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.contracts.Contract;
import com.coincoinche.engine.contracts.ContractFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Custom Deserializer for MoveBidding objects. */
public class MoveBiddingDeserializer extends StdDeserializer<MoveBidding> {

  private static final long serialVersionUID = -639255419145437675L;

  public MoveBiddingDeserializer() {
    this(null);
  }

  public MoveBiddingDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public MoveBidding deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    JsonNode node = p.getCodec().readTree(p);
    String shortName = ((TextNode) node.get("bid")).textValue();
    switch (shortName.toLowerCase()) {
      case "pass":
        return MoveBidding.passMove();
      case "coinche":
        return MoveBidding.coincheMove();
      case "surcoinche":
        return MoveBidding.surcoincheMove();
      default:
        Pattern pattern = Pattern.compile("^(\\d+)([s,h,c,d])$");
        Matcher matcher = pattern.matcher(shortName);
        matcher.matches();
        String valueString = matcher.group(1);
        if (valueString == null) {
          throw ctxt.weirdStringException(
              shortName, MoveBidding.class, "Invalid value for contract move.");
        }
        String suitString = matcher.group(2);
        if (suitString == null) {
          throw ctxt.weirdStringException(
              shortName, MoveBidding.class, "Invalid suit for contract move.");
        }
        Contract contract =
            ContractFactory.createContract(
                Integer.parseInt(valueString), Suit.valueOfByShortName(suitString));
        return MoveBidding.contractMove(contract);
    }
  }
}
