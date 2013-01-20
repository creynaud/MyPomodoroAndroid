Feature: Start pomodoro feature

  Scenario: As a user I can start a pomodoro
    When I touch the "Not started" text
    Then I wait for "Started:" to appear
    And I wait for "Not started" to appear
    
  Scenario: As a user I want to change the pomodoro duration
    When I press the "Settings" action
    Then I press the "Search" action
    And I press the "Test2" action

# TODO need to see what has been done for actionbar tabs that are hidden (cf https://groups.google.com/forum/#!searchin/calabash-android/actionbar/calabash-android/EsQTzGbOblI/sQVOY9v2ZN4J)
# TODOÊtest in a webview
# TODO test in a list and back
# TODO try UIAutomator (see https://groups.google.com/forum/#!searchin/calabash-android/UIautomator/calabash-android/57U7lwkq7cg/pOsucU8-e_EJ)
# TODO get code coverage
