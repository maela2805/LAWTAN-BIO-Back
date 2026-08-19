package com.lawtan.service;

import com.lawtan.dto.FeedRationDTO;
import com.lawtan.dto.FeedStockDTO;
import java.util.List;

public interface FeedService {
    List<FeedStockDTO> getAllFeedStocks();
    FeedStockDTO getFeedStockById(Long id);
    FeedStockDTO updateFeedStockQuantity(Long id, Double newQuantityKg);
    FeedStockDTO createFeedStock(FeedStockDTO dto);
    void deleteFeedStock(Long id);

    List<FeedRationDTO> getAllRations();
    FeedRationDTO getRationById(Long id);
    FeedRationDTO createRation(FeedRationDTO dto);
    FeedRationDTO updateRation(Long id, FeedRationDTO dto);
    void deleteRation(Long id);
}
