import org.apache.commons.collections.CollectionUtils
import java.lang.Integer

def subTaskList = issue.getSubTaskObjects()
def totalEffort = 0
if (subTaskList != null && !CollectionUtils.isEmpty(subTaskList)) {
    for (subTask in subTaskList) {
        if (subTask.getCustomFieldValue('Effort in Day(s)') != null) {
            totalEffort = totalEffort + (subTask.getCustomFieldValue('Effort in Day(s)') as Integer)
        }
    }
}
totalEffort