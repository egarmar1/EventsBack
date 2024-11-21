package com.kike.eventReviews.repository;

import com.kike.eventReviews.entity.EventReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventReviewsRepository extends JpaRepository<EventReview, Long> {
}
