package com.greglturnquist.springagram.backend.domain;

import java.util.List;

import com.greglturnquist.springagram.backend.domain.Item;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_USER')")
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {

    List<Item> findByGalleryIsNull();

    @Override
    @PreAuthorize("#item?.user == null or #item?.user?.name == authentication?.name")
    Item save(@Param("item") Item item);

    @Override
    @PreAuthorize("#item?.user?.name == authentication?.name or hasRole('ROLE_ADMIN')")
    void delete(@Param("item") Item item);

    @Override
    @PreAuthorize("@itemRepository.findOne(#id)?.user?.name == authentication?.name or hasRole('ROLE_ADMIN')")
    void delete(@Param("id") Long id);
}
