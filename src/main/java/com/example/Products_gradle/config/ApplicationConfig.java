package com.example.Products_gradle.config;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.google.gson.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

  @Bean
  public ModelMapper mapper() {
    return new ModelMapper();
  }

  @Bean
  public Gson gson() {
    return new GsonBuilder()
      .excludeFieldsWithoutExposeAnnotation()
      .setPrettyPrinting()
      .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT,
          JsonDeserializationContext context) throws JsonParseException {
          return LocalDate.parse(json.getAsString(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        }
      })
      .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
        @Override
        public JsonElement serialize(LocalDate localDate, Type type,
          JsonSerializationContext jsonSerializationContext) {
          return new JsonPrimitive(
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(localDate));
        }
      })
      .create();
  }

}
