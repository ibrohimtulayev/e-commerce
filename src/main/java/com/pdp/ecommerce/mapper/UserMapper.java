package com.pdp.ecommerce.mapper;

import com.pdp.ecommerce.entity.User;
import com.pdp.ecommerce.model.dto.UserRegisterDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserRegisterDto userRegisterDto);

    UserRegisterDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserRegisterDto userRegisterDto, @MappingTarget User user);

}