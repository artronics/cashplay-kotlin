package com.artronics.cashplay.api

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository
import java.util.*
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "`user`")
data class User(
        @JsonIgnore
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_authorities",
                joinColumns = arrayOf(JoinColumn(name = "user_id", referencedColumnName = "id")),
                inverseJoinColumns = arrayOf(JoinColumn(name = "authority_id", referencedColumnName = "id")))
        var authorities: List<Authority>? = null,

        @Column(length = 50, unique = true)
        @NotNull
        @Size(min = 4, max = 50)
        var username: String? = null,

        @Column(length = 100)
        @NotNull
        @Size(min = 4, max = 100)
        @JsonIgnore
        var password: String? = null,

        var email: String? = null,

        var firstName: String? = null,
        var lastName: String? = null,
        @JsonIgnore
        var enabled: Boolean? = null,

        @Temporal(TemporalType.TIMESTAMP)
        @JsonIgnore
        var lastPasswordResetDate: Date? = null
) : BaseModel()

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
interface UserRepository : PagingAndSortingRepository<User, Long> {
    fun findByEmail(@Param("email") email: String): User?
    fun findByUsername(@Param("username") username: String): User?
}

@Entity
class Authority(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", updatable = false, nullable = false)
        var id: Long? = null,

        @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
        var users: List<User>? = null,

        @Enumerated(EnumType.STRING)
        var name: AuthorityName? = null
) : Serializable

@Repository
interface AuthorityRepository : JpaRepository<Authority, Long> {
    fun findByName(name: AuthorityName): Authority?
}

enum class AuthorityName {
    ROLE_USER,
    ROLE_ADMIN
}
