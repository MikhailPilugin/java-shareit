package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */

//id — уникальный идентификатор запроса;
//description — текст запроса, содержащий описание требуемой вещи;
//requestor — пользователь, создавший запрос;
//created — дата и время создания запроса.

@Data
@Entity
@Table(name = "requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable=false)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "requestor_id")
    private Long requestor;

    @Column(name = "created_date")
    private LocalDate created;
}
