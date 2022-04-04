package com.cx.service.impl;

import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;
import com.cx.fluentmybatis.entity.ProjectEntity;
import com.cx.fluentmybatis.entity.TeacherEntity;
import com.cx.fluentmybatis.mapper.ProjectMapper;
import com.cx.fluentmybatis.mapper.TeacherMapper;
import com.cx.fluentmybatis.wrapper.*;
import com.cx.model.PageReq;
import com.cx.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;




@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    @Transactional(propagation= Propagation.SUPPORTS)
    public List<ProjectEntity> getProjectListByTeacherId(String teacherId){
        ProjectQuery query=new ProjectQuery();
        query.where().teacherId().eq(teacherId).end();
        return projectMapper.listEntity(query);
    }
    @Override
    @Transactional(propagation=Propagation.SUPPORTS)
    public ProjectEntity getProjectByStudentId(String studentId) {
        ProjectQuery query=new ProjectQuery();
        query.where().studentId().eq(studentId).end();
        return projectMapper.findOne(query);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean updateStudentIdByProjectId(String studentId,String projectId) {
        ProjectUpdate update=new ProjectUpdate();
        update.where().projectId().eq(projectId).end().set.studentId().is(studentId).end();
        int res=projectMapper.updateBy(update);
        if (res>=1) return true;
        else return false;
    }
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean updateNoStudent(String projectId) {
        ProjectUpdate update=new ProjectUpdate();
        update.where().projectId().eq(projectId).end().set.studentId().is(null).end();
        int res=projectMapper.updateBy(update);
        if (res>=1) return true;
        else return false;
    }

    @Override
    @Transactional(propagation=Propagation.SUPPORTS)
    public int getCountProjectNum() {
        ProjectQuery query=new ProjectQuery();
        query.where().studentId().isNull().end();
        return projectMapper.count(query);
    }

    // 根据教师id查询该教师已被选的课题数

    @Override
    @Transactional(propagation=Propagation.SUPPORTS)
    public int getTeacherYesProjectNum(String teacherId) {
        ProjectQuery query=new ProjectQuery();
        query.where().teacherId().eq(teacherId).and.studentId().notNull().end();
        return projectMapper.count(query);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean insertSelective(ProjectEntity project,String teacherId) {
        TeacherUpdate update=new TeacherUpdate();
        update.set.teacherProjectNum().applyFunc("teacher_project_num+1").end().where().teacherId().eq(teacherId).end();
        if(teacherMapper.updateBy(update)>0){
            if (projectMapper.insert(project)>0){
                return true;
            }
        }
        return false;
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    public List<ProjectEntity> getProjectByTeacherId(String teacherId) {
        ProjectQuery query=new ProjectQuery();
        query.where().teacherId().eq(teacherId);
        return projectMapper.listEntity(query);
    }



    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public boolean updateByPrimaryKeySelective(ProjectEntity project) {
        if (projectMapper.updateById(project)>0){
            return true;
        }
        return false;
    }

    @Override
    public StdPagedList<ProjectEntity> getPeojectList(PageReq pageReq) {
        ProjectQuery query=new ProjectQuery().selectAll().limit(pageReq.getPageSize()*pageReq.getPageNum(),pageReq.getPageSize());
        return projectMapper.stdPagedEntity(query);
    }
}