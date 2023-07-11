package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.Status;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

//id — уникальный идентификатор бронирования;
//start — дата и время начала бронирования;
//end — дата и время конца бронирования;
//item — вещь, которую пользователь бронирует;
//booker — пользователь, который осуществляет бронирование;
//status — статус бронирования. Может принимать одно из следующих
//значений: WAITING — новое бронирование, ожидает одобрения, APPROVED —
//Дополнительные советы ментора 2
//бронирование подтверждено владельцем, REJECTED — бронирование
//отклонено владельцем, CANCELED — бронирование отменено создателем.

@Data
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable=false)
    private long id;

    @Column(name = "start_date")
    private LocalDate start;

    @Column(name = "end_date")
    private LocalDate end;

    @Column(name = "item_id")
    private Long item;

    @Column(name = "booker_id")
    private Long booker;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
