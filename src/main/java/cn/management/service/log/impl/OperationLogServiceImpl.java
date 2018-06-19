package cn.management.service.log.impl;

import cn.management.domain.log.OperationLog;
import cn.management.mapper.log.OperationLogMapper;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.log.OperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 日志记录Service实现类
 * package: cn.management.service.log.impl
 * project: management
 * </p>
 *
 * @author ZhouJiaKai <zhoujk@pvc123.com>
 * @version v1.0.0
 * @since v1.0.0
 * <p>
 * date 2018/6/19 9:35
 */
@Service
public class OperationLogServiceImpl extends BaseServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
}
