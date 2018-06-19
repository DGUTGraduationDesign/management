package cn.management.service.log;

import cn.management.domain.log.OperationLog;
import cn.management.mapper.log.OperationLogMapper;
import cn.management.service.BaseService;

/**
 * <p>
 * 日志记录Service接口
 * package: cn.management.service.log
 * project: management
 * </p>
 *
 * @author ZhouJiaKai <zhoujk@pvc123.com>
 * @version v1.0.0
 * @since v1.0.0
 * <p>
 * date 2018/6/19 9:27
 */
public interface OperationLogService extends BaseService<OperationLogMapper, OperationLog> {
}
