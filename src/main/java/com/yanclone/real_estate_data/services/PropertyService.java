package com.yanclone.real_estate_data.services;

import io.stargate.grpc.StargateBearerToken;
import io.stargate.proto.StargateGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;

public class PropertyService {
    private final StargateGrpc.StargateBlockingStub blockingStub;

    public PropertyService(String astraDbId, String astraDbRegion, String astraToken, String astraKeyspace) {
        // Cassandra DB connection
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(astraDbId + "-" + astraDbRegion + ".apps.astra.datastax.com", 443)
                .useTransportSecurity()
                .build();

        blockingStub = StargateGrpc.newBlockingStub(channel)
                .withDeadlineAfter(10, TimeUnit.SECONDS)
                .withCallCredentials(new StargateBearerToken(astraToken));
    }
}
