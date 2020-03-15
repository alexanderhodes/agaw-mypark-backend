package me.alexanderhodes.myparkbackend.web.graphql.resolver;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import me.alexanderhodes.myparkbackend.web.graphql.GraphQLDataFetchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

@Component()
public class GraphQLService {

    @Value("${mypark.graphql.schema}")
    private String schemaName;
    @Autowired
    private GraphQLDataFetchers graphQLDataFetchers;

    private GraphQL graphQL;

    @Bean
    public GraphQL getGraphQL() {
        return this.graphQL;
    }

    @PostConstruct
    private void init() throws IOException {
        URL url = Resources.getResource(schemaName);
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);

        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring.newTypeWiring("Query")
                        .dataFetcher("userById", graphQLDataFetchers.getUserByIdDataFetcher())
                        .dataFetcher("allUsers", graphQLDataFetchers.getUsersDataFetcher())
                        .dataFetcher("findParkingSpaceById", graphQLDataFetchers.getParkingSpaceDataFetcher())
                        .dataFetcher("allParkingSpaces", graphQLDataFetchers.getParkingSpacesDataFetcher())
                        .dataFetcher("findBookings", graphQLDataFetchers.getBookingsDataFetcher())
                        .dataFetcher("findBookingsByUser", graphQLDataFetchers.getBookingsByUserDataFetcher())
                        .dataFetcher("findAbsencesByUser", graphQLDataFetchers.getAbsencesByUserDataFetcher())
                        .dataFetcher("findSeriesBookingsByUser", graphQLDataFetchers.getSeriesBookingsByUserDataFetcher())
                        .dataFetcher("findSeriesAbsenceByUser", graphQLDataFetchers.getSeriesAbsencesByUserDataFetcher())
                        .dataFetcher("createUser", graphQLDataFetchers.createUserDataFetcher())
                )
//                .type(TypeRuntimeWiring.newTypeWiring("Mutation")
//                        .dataFetcher("createUser", graphQLDataFetchers.createUserDataFetcher()))
                .type(TypeRuntimeWiring.newTypeWiring("User")
                        .dataFetcher("parkingSpace", graphQLDataFetchers.getParkingSpaceDataFetcher()))
                .type(TypeRuntimeWiring.newTypeWiring("ParkingSpace")
                        .dataFetcher("parkingSpaceStatus", graphQLDataFetchers.getParkingSpaceStatusDataFetcher()))
                .build();
    }
}
