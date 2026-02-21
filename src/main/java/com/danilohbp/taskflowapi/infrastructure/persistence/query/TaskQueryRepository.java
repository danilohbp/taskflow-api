package com.danilohbp.taskflowapi.infrastructure.persistence.query;

import com.danilohbp.taskflowapi.domain.model.Task;
import com.danilohbp.taskflowapi.infrastructure.persistence.query.dto.TaskListItemView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TaskQueryRepository extends Repository<Task, Long> {

    @Query("""
        select new com.danilohbp.taskflowapi.infrastructure.persistence.query.dto.TaskListItemView(
            t.id,
            t.title,
            t.status,
            t.priority,
            t.dueDate,
            a.id,
            a.name,
            r.id,
            r.name
        )
        from Task t
        left join t.assignee a
        join t.reporter r
        order by t.updatedAt desc
    """)
    List<TaskListItemView> listForTasksPage();

}
