package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @InjectMocks
    ItemRequestServiceImpl itemRequestService;
    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    UserService userService;
    @Mock
    ItemRepository itemRepository;
    ItemRequestDto itemRequestDto;
    ItemRequest itemRequest;
    User user;
    Item item;
    ItemRequestMapper itemRequestMapper = new ItemRequestMapper(new ItemMapper());
    Random random = new Random();


    @BeforeEach
    void setUp() {
        itemRequestService.setItemRequestMapper(itemRequestMapper);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Request description");
        itemRequestDto.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

        user = new User();
        user.setId((long) random.nextInt(32));
        user.setName("User name");
        user.setEmail("user@email.com");

        itemRequest = new ItemRequest();
        itemRequest.setId((long) random.nextInt(32));
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        itemRequest.setUser(user);

        item = new Item();
        item.setId((long) random.nextInt(32));
        item.setName("Item name");
        item.setDescription("Item description");
        item.setAvailable(Boolean.TRUE);
        item.setOwner(user);
        item.setItemRequest(itemRequest);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void add() {
        long itemRequestId = random.nextInt(32);
        when(userService.getById(anyLong())).thenReturn(user);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenAnswer(invocationOnMock -> {
            ItemRequest argItemRequest = invocationOnMock.getArgument(0, ItemRequest.class);
            argItemRequest.setId(itemRequestId);
            return argItemRequest;
        });

        ItemRequestDto returnedDto = itemRequestService.add(user.getId(), itemRequestDto);

        assertThat(returnedDto, notNullValue());
        assertThat(returnedDto.getId(), equalTo(itemRequestId));
        verify(userService).getById(user.getId());
        verify(itemRequestRepository).save(any());
    }

    @Test
    void getById_whenItemRequestIdNotExist_thenThrowItemRequestNotFoundException() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        ItemRequestNotFoundException exception = assertThrows(ItemRequestNotFoundException.class, () ->
                itemRequestService.getById(user.getId(), itemRequest.getId()));

        assertThat(exception.getMessage(), equalTo("Request with id=" + itemRequest.getId() + " not found"));
        verify(itemRepository, never()).findAllByItemRequestId(anyLong());
    }

    @Test
    void getById() {
        doNothing().when(userService).checkUser(anyLong());
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByItemRequestId(anyLong())).thenReturn(List.of(item));

        ItemRequestDto returnedDto = itemRequestService.getById(user.getId(), itemRequest.getId());

        assertThat(returnedDto, notNullValue());
        assertThat(returnedDto.getId(), equalTo(itemRequest.getId()));
        verify(userService).checkUser(user.getId());
        verify(itemRequestRepository).findById(itemRequest.getId());
        verify(itemRepository).findAllByItemRequestId(itemRequest.getId());
    }

    @Test
    void getOwn() {
        doNothing().when(userService).checkUser(anyLong());
        when(itemRequestRepository.findAllByUserIdOrderByCreatedDesc(anyLong())).thenReturn(List.of(itemRequest));
        when(itemRepository.findAllByItemRequestId(anyCollection())).thenReturn(List.of(item));

        List<ItemRequestDto> result = itemRequestService.returnUserRequests(user.getId());

        assertThat(result, allOf(notNullValue(), hasSize(1)));
        assertThat(result, hasItem(itemRequestMapper.ItemRequestDto(itemRequest)));
        verify(userService).checkUser(user.getId());
        verify(itemRequestRepository).findAllByUserIdOrderByCreatedDesc(user.getId());
        verify(itemRepository).findAllByItemRequestId(Set.of(itemRequest.getId()));
    }

    @Test
    void getAll() {
        when(itemRequestRepository.findByUserIdNot(anyLong(), any())).thenReturn(new PageImpl<>(List.of(itemRequest)));
        when(itemRepository.findAllByItemRequestId(anyCollection())).thenReturn(List.of(item));

        List<ItemRequestDto> result = itemRequestService.getAll(user.getId(), 0, 2);

        assertThat(result, allOf(notNullValue(), hasSize(1)));
        assertThat(result, hasItem(itemRequestMapper.ItemRequestDto(itemRequest)));
        assertThat(result, equalTo(itemRequestMapper.ItemRequestDto(List.of(itemRequest))));
        verify(itemRequestRepository).findByUserIdNot(user.getId(),
                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "created")));
        verify(itemRepository).findAllByItemRequestId(Set.of(itemRequest.getId()));
    }
}