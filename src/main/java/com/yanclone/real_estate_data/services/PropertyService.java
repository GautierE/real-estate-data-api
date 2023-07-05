package com.yanclone.real_estate_data.services;

import io.stargate.proto.QueryOuterClass;
import io.stargate.grpc.StargateBearerToken;
import io.stargate.proto.StargateGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.yanclone.real_estate_data.models.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
                .withDeadlineAfter(10, TimeUnit.SECONDS)
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
}
