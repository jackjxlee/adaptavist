import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.MutableIssue
import org.apache.commons.collections.CollectionUtils
import com.atlassian.jira.issue.Issue

def issueManager = ComponentAccessor.issueManager
Issue issue = event.getIssue() as MutableIssue
def issueComments = issue.comments
def user = Users.getByName('jack.jxlee')
if (issueComments != null && !CollectionUtils.isEmpty(issueComments)){
    issue.setDescription(issueComments.last().body)
    issueManager.updateIssue(user, issue, EventDispatchOption.DO_NOT_DISPATCH, false)
}