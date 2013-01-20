Then /^I press the "([^\"]*)" action$/ do |item|
  begin
    performAction('press', item)
  rescue Exception => e
    performAction('select_from_menu', item)
  end
end