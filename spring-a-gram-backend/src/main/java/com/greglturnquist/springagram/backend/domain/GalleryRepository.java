package com.greglturnquist.springagram.backend.domain;

import com.greglturnquist.springagram.backend.domain.Gallery;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GalleryRepository extends PagingAndSortingRepository<Gallery, Long> {
}
