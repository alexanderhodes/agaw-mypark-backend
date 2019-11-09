package me.alexanderhodes.myparkbackend.model;

import javax.persistence.Column;
import java.util.Date;

public abstract class CommonEntity {

    @Column(name = "createTimestamp")
    private Date createTimestamp;
    @Column(name = "lastUpdateTimestamp")
    private Date lastUpdateTimestamp;
    @Column(name = "deleteTimestamp")
    private Date deleteTimestamp;

}
