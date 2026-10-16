package com.acme.backoffice.model;

import java.util.List;

/** The shipments to customers of a region shipped in a period: those still in transit and those delivered late, oldest first. */
public record RegionShipments(Region region, List<Shipment> inTransit, List<Shipment> late) {
}
