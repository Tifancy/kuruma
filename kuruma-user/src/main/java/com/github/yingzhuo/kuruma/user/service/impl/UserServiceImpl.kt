package com.github.yingzhuo.kuruma.user.service.impl

import com.github.yingzhuo.kuruma.common.entity.User
import com.github.yingzhuo.kuruma.common.exception.ResourceNotFoundException
import com.github.yingzhuo.kuruma.user.controller.request.UserRequest
import com.github.yingzhuo.kuruma.user.dao.UserDao
import com.github.yingzhuo.kuruma.user.service.UserService
import org.apache.commons.beanutils.PropertyUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class UserServiceImpl @Autowired constructor(val userDao: UserDao): UserService {

    override fun findUserById(userId: String): User {
        return userDao.findUserById(userId) ?: throw ResourceNotFoundException()
    }

    override fun regiesterUser(request: UserRequest): User {
        val user = User()
        PropertyUtils.copyProperties(user, request)
        return userDao.saveUser(user)
    }

}