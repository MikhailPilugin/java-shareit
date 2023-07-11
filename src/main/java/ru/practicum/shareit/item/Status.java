package ru.practicum.shareit.item;

//status — статус бронирования. Может принимать одно из следующих
//значений: WAITING — новое бронирование, ожидает одобрения, APPROVED —
//Дополнительные советы ментора 2
//бронирование подтверждено владельцем, REJECTED — бронирование
//отклонено владельцем, CANCELED — бронирование отменено создателем.

public enum Status {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED
}
