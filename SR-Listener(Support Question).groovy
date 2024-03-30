import com.atlassian.jira.issue.IssueInputParametersImpl
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue

Issue issue = event.getIssue()
if (issue.issueType.name != "Bug") {
    return
}
createSubtask(issue, "Reproduction steps")
createSubtask(issue, "UAT")
createTask(issue, "Cost in hours")

def createSubtask(Issue parentIssue, String subTaskSummary) {
    def subTaskManager = ComponentAccessor.subTaskManager
    def asUser = ComponentAccessor.jiraAuthenticationContext.loggedInUser
    def constantsManager = ComponentAccessor.constantsManager
    def issueService = ComponentAccessor.issueService

    def subtaskIssueType = constantsManager.allIssueTypeObjects.findByName('Sub-task')

    assert subtaskIssueType?.subTask

    // Fill the required fields
    def issueInputParameters = new IssueInputParametersImpl()
    issueInputParameters
        .setProjectId(parentIssue.projectId)
        .setIssueTypeId(subtaskIssueType.id)
        .setSummary(parentIssue.summary + ": " + subTaskSummary)
        .setReporterId(asUser.username)

    def createValidationResult = ComponentAccessor.issueService.validateSubTaskCreate(asUser, parentIssue.id, issueInputParameters)
    if (!createValidationResult.valid) {
        log.error createValidationResult.errorCollection
        return
    }

    def subIssue = issueService.create(asUser, createValidationResult).issue
    subTaskManager.createSubTaskIssueLink(parentIssue, subIssue, asUser)
}

def createTask(Issue parentIssue, String subTaskSummary) {
    def taskIssue = Issues.create(parentIssue.projectObject.getKey(), "Task") {
        setSummary(issue.summary + ": " + subTaskSummary)
    }
    parentIssue.link("is caused by", taskIssue)
}