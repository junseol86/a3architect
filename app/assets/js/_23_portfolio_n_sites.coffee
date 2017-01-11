$ ->
  $('#select_all').click () ->
    $('option').removeAttr('selected')
    $('#select_yongdo option:eq(0)').attr 'selected', 'selected'
    $('#select_gujo option:eq(0)').attr 'selected', 'selected'
    $('#select_year option:eq(0)').attr 'selected', 'selected'
    $('#select_gyumo option:eq(0)').attr 'selected', 'selected'


@getExtraHash = () ->
  '^' + getYongdo() + '^' + getYear() + '^' + getGujo + '^' + getGyumo

@getYongdo = () ->
  if (location.hash.replace('#', '') != '')
    hashData = location.hash
    hashData.replace('#', '').split('^')[1]
  else
    $('#select_yongdo').val()

@getYear = () ->
  if (location.hash.replace('#', '') != '')
    hashData = location.hash
    hashData.replace('#', '').split('^')[2]
  else
    $('#select_year').val()
    
@getGujo = () ->
  if (location.hash.replace('#', '') != '')
    hashData = location.hash
    hashData.replace('#', '').split('^')[3]
  else
    $('#select_gujo').val()
    
@getGyumo = () ->
  if (location.hash.replace('#', '') != '')
    hashData = location.hash
    hashData.replace('#', '').split('^')[4]
  else
    $('#select_gyumo').val()

@getPortfolioOrSitesList = () ->
  page = boardModule().getCurrentPage()
  url = $('#list_container').data("url")
  $.post url,
    {
      yongdo: getYongdo()
      year: getYear()
      gujo: getGujo()
      gyumo: getGyumo()
      page: page
    }
    (data, status) ->
      boardModule().afterAjaxLoad(data)
