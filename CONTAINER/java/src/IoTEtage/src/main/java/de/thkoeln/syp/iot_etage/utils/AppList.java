package de.thkoeln.syp.iot_etage.utils;

import java.util.List;
import java.util.Map;

public class AppList {
  public static List<Map<String, String>> appList = List.of(
    Map.of(
      "name", "Sensors",
      "icon", "fa-microchip",
      "url", "/sensors"
    ),
    Map.of(
      "name", "Events",
      "icon", "fa-history",
      "url", "/events"
    ),
    Map.of(
      "name", "Beleuchtungssteuerung",
      "icon", "fa-sun",
      "url", "/beleuchtungssteuerung"
    ),
    Map.of(
      "name", "Markisolettenstuerung",
      "icon", "fa-arrows-alt-v",
      "url", "/markisolettensteuerung"
    ),
    Map.of(
      "name", "Belüftungssteuerung",
      "icon", "fa-fan",
      "url", "/belueftungssteuerung"
    ),
    Map.of(
      "name", "Raumstatus",
      "icon", "fa-info",
      "url", "/roomstatus"
    )
  );
}