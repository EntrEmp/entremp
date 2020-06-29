package com.entremp.core.entremp.data.notification

import com.entremp.core.entremp.model.notification.Notification
import com.entremp.core.entremp.model.user.User

import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.*

data class NotificationFilterSpecification(
    val user: User?,
    val seen: Boolean = false
): Specification<Notification> {

    override fun toPredicate(root: Root<Notification>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {

        val predicates: MutableList<Predicate> = mutableListOf()
        query.distinct(true)

        if(user != null){
            val userId: Path<String> = root.get<String>("user_id")
            predicates.add(
                builder.equal(
                    userId , user.id
                )
            )
        }

        val isSeen: Path<Boolean> = root.get<Boolean>("seen")
        predicates.add(
            builder.equal(
                isSeen , seen
            )
        )

        val predicate: Predicate? = builder.and(*predicates.toTypedArray())

        return predicate
    }
}