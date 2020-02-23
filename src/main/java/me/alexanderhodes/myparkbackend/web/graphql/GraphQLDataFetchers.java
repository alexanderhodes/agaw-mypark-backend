package me.alexanderhodes.myparkbackend.web.graphql;

import graphql.schema.DataFetcher;
import me.alexanderhodes.myparkbackend.model.ParkingSpace;
import me.alexanderhodes.myparkbackend.model.ParkingSpaceStatus;
import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component()
public class GraphQLDataFetchers {

    private static List<User> users = Arrays.asList(
            new User("1", "alex", "password", "alexander.hodes@live.com", "Alexander",
                    "Hodes", true, new ParkingSpace("1"), ""),
            new User("2", "max", "password", "mail@example.com", "Max",
                    "Mustermann", true, new ParkingSpace("1"), ""),
            new User("3", "theo", "password", "mail@mail.com", "Theo",
                    "Hodes", true, new ParkingSpace("1"), ""),
            new User("4", "frank", "password", "mail1@mail.com", "Frank",
                    "Wagner", true, new ParkingSpace("2"), "")
    );

    private static List<ParkingSpace> parkingSpaces = Arrays.asList(
            new ParkingSpace("1", "78", new ParkingSpaceStatus("1", "free", "green"))
    );

    private static List<ParkingSpaceStatus> parkingSpaceStatuses = Arrays.asList(
            new ParkingSpaceStatus("1", "free", "green"),
            new ParkingSpaceStatus("2", "used", "red")
    );

    public DataFetcher getUsersDataFetcher() {
        return dataFetchingEnvironment -> {
            return users;
        };
    }

    public DataFetcher getUserByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("id");
            return users
                    .stream()
                    .filter(user -> user.getId().equals(userId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getParkingSpaceDataFetcher() {
        return dataFetchingEnvironment -> {
            User user = dataFetchingEnvironment.getSource();
            String parkingSpaceId = user.getParkingSpace().getId();
            return parkingSpaces
                    .stream()
                    .filter(parkingSpace -> parkingSpace.getId().equals(parkingSpaceId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getParkingSpaceStatusDataFetcher() {
        return dataFetchingEnvironment -> {
            ParkingSpace parkingSpace = dataFetchingEnvironment.getSource();
            String parkingSpaceStatusId = parkingSpace.getParkingSpaceStatus().getId();
            return parkingSpaceStatuses
                    .stream()
                    .filter(parkingSpaceStatus -> parkingSpaceStatus.getId().equals(parkingSpaceStatusId))
                    .findFirst()
                    .orElse(null);
        };
    }

}
