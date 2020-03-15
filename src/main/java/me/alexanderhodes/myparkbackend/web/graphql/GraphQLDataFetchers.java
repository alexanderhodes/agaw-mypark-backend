package me.alexanderhodes.myparkbackend.web.graphql;

import graphql.schema.DataFetcher;
import me.alexanderhodes.myparkbackend.model.*;
import me.alexanderhodes.myparkbackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component()
public class GraphQLDataFetchers {

    @Autowired
    private UserService userService;
    @Autowired
    private ParkingSpaceService parkingSpaceService;
    @Autowired
    private ParkingSpaceStatusService parkingSpaceStatusService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private AbsenceService absenceService;
    @Autowired
    private SeriesBookingService seriesBookingService;
    @Autowired
    private SeriesAbsenceService seriesAbsenceService;

    public DataFetcher<List<User>> getUsersDataFetcher() {
        return dataFetchingEnvironment -> this.userService.findAll();
    }

    public DataFetcher<User> getUserByIdDataFetcher() {
        return dataFetchingEnvironment -> dataFetchingEnvironment.getArgument("userId") != null ?
                this.userService.findById(dataFetchingEnvironment.getArgument("id")).orElse(null) : null;
    }

    public DataFetcher<ParkingSpace> getParkingSpaceDataFetcher() {
        return dataFetchingEnvironment -> {
            User user = dataFetchingEnvironment.getSource();
            return user.getParkingSpace() != null ? this.parkingSpaceService.findById(user.getParkingSpace().getId()).orElse(null) : null;
        };
    }

    public DataFetcher<List<Booking>> getBookingsDataFetcher() {
        return dataFetchingEnvironment -> this.bookingService.findAll();
    }

    public DataFetcher<List<ParkingSpace>> getParkingSpacesDataFetcher() {
        return dataFetchingEnvironment -> this.parkingSpaceService.findAllByOrderByNumber();
    }

    public DataFetcher<ParkingSpaceStatus> getParkingSpaceStatusDataFetcher() {
        return dataFetchingEnvironment -> {
            ParkingSpace parkingSpace = dataFetchingEnvironment.getSource();
            return parkingSpace.getParkingSpaceStatus() != null ? this.parkingSpaceStatusService.findById(parkingSpace.getParkingSpaceStatus().getId()).orElse(null) : null;
        };
    }

    public DataFetcher<List<Booking>> getBookingsByUserDataFetcher() {
        return dataFetchingEnvironment -> {
            if (dataFetchingEnvironment.getArgument("userId") == null) {
                return new ArrayList<>();
            }

            Optional<User> user = this.userService.findById(dataFetchingEnvironment.getArgument("userId"));
            return user.isPresent() ? this.bookingService.findByUserOrderByDateAsc(user.get()) : new ArrayList<>();
        };
    }

    public DataFetcher<List<Absence>> getAbsencesByUserDataFetcher() {
        return dataFetchingEnvironment -> {
            if (dataFetchingEnvironment.getArgument("userId") == null) {
                return new ArrayList<>();
            }

            Optional<User> user = this.userService.findById(dataFetchingEnvironment.getArgument("userId"));
            return user.isPresent() ? this.absenceService.findByUser(user.get()) : new ArrayList<>();
        };
    }

    public DataFetcher<List<SeriesBooking>> getSeriesBookingsByUserDataFetcher() {
        return dataFetchingEnvironment -> {
            if (dataFetchingEnvironment.getArgument("userId") == null) {
                return new ArrayList<>();
            }

            Optional<User> user = this.userService.findById(dataFetchingEnvironment.getArgument("userId"));
            return user.isPresent() ? this.seriesBookingService.findByUser(user.get()) : new ArrayList<>();
        };
    }

    public DataFetcher<List<SeriesAbsence>> getSeriesAbsencesByUserDataFetcher() {
        return dataFetchingEnvironment -> {
            if (dataFetchingEnvironment.getArgument("userId") == null) {
                return new ArrayList<>();
            }

            Optional<User> user = this.userService.findById(dataFetchingEnvironment.getArgument("userId"));
            return user.isPresent() ? this.seriesAbsenceService.findByUser(user.get()) : new ArrayList<>();
        };
    }

    public DataFetcher<User> createUserDataFetcher() {
        return dataFetchingEnvironment -> {
            String name = dataFetchingEnvironment.getArgument("name");
            String password = dataFetchingEnvironment.getArgument("password");
            String username = dataFetchingEnvironment.getArgument("username");
            String firstName = dataFetchingEnvironment.getArgument("firstName");
            String lastName = dataFetchingEnvironment.getArgument("lastName");
            String privateEmail = dataFetchingEnvironment.getArgument("privateEmail");

            return new User(null, name, password, username, firstName, lastName, false, null, privateEmail);
        };
    }

}
