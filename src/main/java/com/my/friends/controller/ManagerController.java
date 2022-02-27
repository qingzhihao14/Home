package com.my.friends.controller;

import cn.hutool.json.JSONUtil;
import com.my.friends.dao.*;
import com.my.friends.mapper.SqlService;
import com.my.friends.service.CommandService;
import com.my.friends.service.PersonService;
import com.my.friends.utils.CodeMsg;
import com.my.friends.utils.Result;
import com.my.friends.utils.pageHelper.PageRequest;
import com.my.friends.utils.pay.JsCodeSession;
import com.my.friends.utils.pay.WeiXinUtil;
import com.mysql.jdbc.StringUtils;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/home")
@Api(description = "信息管理", hidden=true)
public class ManagerController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CommandService commandService;

    @Autowired
    private PersonService personService;

    @Resource
    SqlService sqlService;

    private static final
    org.apache.commons.logging.Log log= LogFactory.getLog(ManagerController.class);

    /*
    * 获取微信用户openid
    * */
    public Result getOpenId(HttpServletRequest request){

        String token = request.getHeader("token");
        log.info("saveLoginInfo获取header_token="+token);
        if(!StringUtils.isNullOrEmpty(token)){
            String cookiez = redisTemplate.opsForValue().get(token);
            log.info("saveLoginInfo_通过redis获取到的session值="+cookiez);
            if(!StringUtils.isNullOrEmpty(cookiez)){
                String[] strings = cookiez.split("，");
                if(strings.length<=0){
                    return Result.error(CodeMsg.SESSION_NOT_EXSIST,"Token无效");
                }
                User user = sqlService.getUser(strings[1]);
                return Result.success(user);
            }else{
                return Result.error(CodeMsg.SESSION_NOT_EXSIST,"Token无效");
            }
        }else{
            return Result.error(CodeMsg.SESSION_NOT_EXSIST,"Token无效");
        }
    }
    /*
     * 0.获取类别、项目信息
     * */
    @ApiOperation(value = "获取类别、项目信息")
    @GetMapping("/getLbXmz")
    public Result getLbXms(HttpServletRequest request,
                           @ApiParam(value = "类别号(传空查询所有)",required = false,defaultValue = "LB1")
                           @RequestParam(required = false) String parent){

        return personService.selectLbXm(parent);
    }
    /*
     * 0.获取订单信息
     * */
    @PostMapping(value="/findPage")
    public Result findPage(@RequestBody PageRequest pageRequest) {
        return Result.success(personService.findPage(pageRequest));
    }
    /*
     * 01.获取用户
     * */
    @PostMapping(value="/findUsersPage")
    public Result findUsersPage(@RequestBody PageRequest pageRequest) {
        return Result.success(personService.findUsersPage(pageRequest));
    }
    /*
     * 0.获取订单信息
     * */
    @PostMapping(value="/taskFindOrders")
    public Result taskFindOrders(@RequestBody PageRequest pageRequest) {
        return Result.success(personService.taskFindOrders(pageRequest));
    }
    /*
     * 0.获取日志信息
     * */
    @PostMapping(value="/findLogPage")
    public Result findLogPage(@RequestBody PageRequest pageRequest) {
        return Result.success(personService.findLogPage(pageRequest));
    }


}
