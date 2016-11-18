$ ->
  $('.people .photo').each( () ->
#    $(this).css 'background-color', 'yellow'
    $(this).css 'background-image', "url('/assets//images/contents/people/" + this.id + ".jpg')"
    $(this).children('img').css 'background-image', "url('/assets//images/contents/people/" + this.id + ".png')"
  )

  $('.people > div').each( () ->
    idReplace = $(this).attr('id').replace(' ', '').replace('&', '')
    $(this).attr 'id', idReplace
  )

  $('.filters .items > div').click () ->
    idToShow = $(this).attr 'id'
    $('.people > div').removeClass '-on'
    $('.people > div#' + idToShow).addClass '-on'

  $('.filters #all').click ->
    $('.people > div').addClass '-on'

