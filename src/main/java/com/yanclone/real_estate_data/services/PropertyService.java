package com.yanclone.real_estate_data.services;

import com.yanclone.real_estate_data.models.Property;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.stargate.grpc.StargateBearerToken;
import io.stargate.proto.QueryOuterClass;
import io.stargate.proto.StargateGrpc;

import java.util.ArrayList;
import java.util.List;

public class PropertyService {
    private final StargateGrpc.StargateBlockingStub blockingStub;
    private final String astraKeySpace;

    public PropertyService(String astraDbId, String astraDbRegion, String astraToken, String astraKeyspace) {
        this.astraKeySpace = astraKeyspace;
        // Cassandra DB connection
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(astraDbId + "-" + astraDbRegion + ".apps.astra.datastax.com", 443)
                .useTransportSecurity()
                .build();

        blockingStub = StargateGrpc.newBlockingStub(channel)
                .withCallCredentials(new StargateBearerToken(astraToken));
    }

    public List<Property> getAllProperties() {
        List<Property> properties = new ArrayList<>();

        QueryOuterClass.Response propertiesQuery = blockingStub.executeQuery(QueryOuterClass
                .Query.newBuilder()
                .setCql("SELECT * FROM " + this.astraKeySpace + ".property")
                .build());

        for (QueryOuterClass.Row row : propertiesQuery.getResultSet().getRowsList()) {
            Property property = new Property
                    (
                            (int) row.getValues(0).getInt(),
                            row.getValues(7).getString(),
                            row.getValues(1).getString(),
                            row.getValues(4).getString(),
                            row.getValues(8).getString(),
                            (int) row.getValues(5).getInt(),
                            row.getValues(6).getInt(),
                            (int) row.getValues(3).getInt(),
                            (int) row.getValues(2).getInt(),
                            (int) row.getValues(9).getInt()
                    );
            properties.add(property);
        }

        return properties;
    }

    public Property getPropertyById(String propertyId) {
        Property property = null;

        QueryOuterClass.Response queryString = blockingStub.executeQuery(QueryOuterClass
                .Query.newBuilder()
                .setCql("SELECT * FROM " + this.astraKeySpace + ".property WHERE property_id = " + propertyId)
                .build());

        if (queryString.getResultSet().getRowsCount() > 0) {
            QueryOuterClass.Row row = queryString.getResultSet().getRows(0);
            property = new Property(
                    (int) row.getValues(0).getInt(),
                    row.getValues(7).getString(),
                    row.getValues(1).getString(),
                    row.getValues(4).getString(),
                    row.getValues(8).getString(),
                    (int) row.getValues(5).getInt(),
                    row.getValues(6).getInt(),
                    (int) row.getValues(3).getInt(),
                    (int) row.getValues(2).getInt(),
                    (int) row.getValues(9).getInt()
            );
        }

        return property;
    }

    public Property createProperty(Property property) {
        String cql = "INSERT INTO " + this.astraKeySpace + ".property " +
                "(property_id, property_type, address, city, state, postal_code, price, bedrooms, bathrooms, year_built) " +
                "VALUES (" +
                property.getPropertyId() + ", '" +
                property.getPropertyType() + "', '" +
                property.getAddress() + "', '" +
                property.getCity() + "', '" +
                property.getState() + "', " +
                property.getPostalCode() + ", " +
                property.getPrice() + ", " +
                property.getBedrooms() + ", " +
                property.getBathrooms() + ", " +
                property.getYearBuilt() +
                ")";

        blockingStub.executeQuery(QueryOuterClass.Query.newBuilder().setCql(cql).build());

        return property;
    }

    public Property updateProperty(String propertyId, Property updatedProperty) {
        String cql = "UPDATE " + this.astraKeySpace + ".property SET " +
                "property_type = '" + updatedProperty.getPropertyType() + "', " +
                "address = '" + updatedProperty.getAddress() + "', " +
                "city = '" + updatedProperty.getCity() + "', " +
                "state = '" + updatedProperty.getState() + "', " +
                "postal_code = " + updatedProperty.getPostalCode() + ", " +
                "price = " + updatedProperty.getPrice() + ", " +
                "bedrooms = " + updatedProperty.getBedrooms() + ", " +
                "bathrooms = " + updatedProperty.getBathrooms() + ", " +
                "year_built = " + updatedProperty.getYearBuilt() +
                " WHERE property_id = " + propertyId;

        blockingStub.executeQuery(QueryOuterClass.Query.newBuilder().setCql(cql).build());

        return updatedProperty;
    }

    public boolean deleteProperty(String propertyId) {
        String cql = "DELETE FROM " + this.astraKeySpace + ".property WHERE property_id=" + propertyId + " IF EXISTS";
        QueryOuterClass.Response response = blockingStub.executeQuery(QueryOuterClass.Query.newBuilder().setCql(cql).build());

        return response != null;
    }
}
