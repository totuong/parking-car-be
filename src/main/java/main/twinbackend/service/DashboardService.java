package main.twinbackend.service;

import lombok.RequiredArgsConstructor;
import main.twinbackend.ParkingWebSocketHandler;
import main.twinbackend.dto.ParkingRealtimeMessage;
import main.twinbackend.dto.ParkingSlotHistoryDto;
import main.twinbackend.entity.ParkingSlotHistory;
import main.twinbackend.repository.ParkingSlotHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ParkingSlotHistoryRepository historyRepository;
    private final ParkingWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public ParkingRealtimeMessage getDashboardMessage() {
        List<ParkingSlotHistoryDto> activeSlots = historyRepository.findByStatus("active")
                .stream()
                .map(this::toHistoryDto)
                .toList();

        List<ParkingSlotHistoryDto> history = historyRepository.findTop10ByOrderByTimestampDesc()
                .stream()
                .map(this::toHistoryDto)
                .toList();

        return ParkingRealtimeMessage.builder()
                .slots(activeSlots)
                .history(history)
                .build();
    }

    public List<ParkingSlotHistoryDto> getSlots() {

        return historyRepository.findByStatus("active")
                .stream()
                .map(this::toHistoryDto)
                .toList();
    }

    public Page<ParkingSlotHistoryDto> getHistories(Pageable pageable) {
        Page<ParkingSlotHistory> parkingSlotHistoryPage = historyRepository.findByStatus("inactive", pageable);
        return parkingSlotHistoryPage.map(this::toHistoryDto);
    }

    public void pushDashboard() {
        ParkingRealtimeMessage dto = getDashboardMessage();
        webSocketHandler.broadcast(objectMapper.writeValueAsString(dto));
    }

    private ParkingSlotHistoryDto toHistoryDto(ParkingSlotHistory e) {
        if (e == null)
            return null;
        return ParkingSlotHistoryDto.builder()
                .frameId(e.getFrameId())
                .id(e.getId())
                .occupied(e.getOccupied())
                .timestamp(e.getTimestamp())
                .startDate(e.getStartDate())
                .status(e.getStatus())
                .build();
    }

}
