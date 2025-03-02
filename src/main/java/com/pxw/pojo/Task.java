package com.pxw.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by pxw on 2022/4/22 15:11
 *
 * @author pxw
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {
    private Integer id;
    private Cclasses cclasses;
    private Teacher teacher;
    private Integer startWeek;
    private Integer endWeek;
    private Integer weekNode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cclasses getCclasses() {
        return cclasses;
    }

    public void setCclasses(Cclasses cclasses) {
        this.cclasses = cclasses;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Integer getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(Integer startWeek) {
        this.startWeek = startWeek;
    }



    public Integer getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(Integer endWeek) {
        this.endWeek = endWeek;
    }

    public Integer getWeekNode() {
        return weekNode;
    }

    public void setWeekNode(Integer weekNode) {
        this.weekNode = weekNode;
    }

    // 判断该任务的课程是否需要连排
    public boolean requiresConsecutive() {
        // 从 cclasses 关联的 Course 中获取 remark 属性
        String remark = this.getCclasses().getCourse().getRemark();
        // 当 remark 为 "A" 或 "B" 时认为需要连排
        return "A".equals(remark) || "B".equals(remark);
    }

    // 返回需要连续的节数，如果不需要连排则返回 0
    public int getConsecutiveCount() {
        String remark = this.getCclasses().getCourse().getRemark();
        if ("A".equals(remark)) {
            return 2;
        } else if ("B".equals(remark)) {
            return 4;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", cclasses=" + cclasses +
                ", teacher=" + teacher +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", weekNode=" + weekNode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (cclasses.getId() != null ? !cclasses.getId().equals(task.cclasses.getId()) : task.cclasses.getId() != null) return false;
        return weekNode != null ? weekNode.equals(task.weekNode) : task.weekNode == null;
    }

}
