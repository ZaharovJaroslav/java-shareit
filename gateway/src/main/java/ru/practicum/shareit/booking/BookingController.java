package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.BadRequestException;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
											      @RequestParam(name = "state", defaultValue = "all")
												  String stateParam) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		return bookingClient.findAllByUserId(userId, state);
	}

	@PostMapping
	public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
										     @RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.addBooking(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> findById(@RequestHeader("X-Sharer-User-Id") Long userId,
										   @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.findById(userId, bookingId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approve(@RequestHeader("X-Sharer-User-Id") Long userId,
			                              @PathVariable Long bookingId,
										  @RequestParam Boolean approved) {
		Object brookingId;
		log.info("patch approved userId={} bookingId={}", userId, bookingId);
		return bookingClient.approve(bookingId, userId, approved);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> findAllByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId,
												   @RequestParam(defaultValue = "ALL") String state) {
		BookingState stateParam = BookingState.from(state)
				.orElseThrow(() -> new BadRequestException("Unknown state: " + state));
		return bookingClient.findAllByOwnerId(userId, stateParam);
	}
}