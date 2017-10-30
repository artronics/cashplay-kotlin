package com.artronics.cashplay.api

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import javax.persistence.Entity

@Entity
data class Customer(
        val firstName: String? = null,
        val lastName: String? = null
) : BaseModel()

@RepositoryRestResource(collectionResourceRel = "customers", path = "customers")
interface CustomerRepository : PagingAndSortingRepository<Customer, Long>