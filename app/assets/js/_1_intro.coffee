$ ->
#  A3 사람들
  $('.people_gallery .photo').each( () ->
    $(this).css 'background-image', "url('/assets/images/contents/people/" + this.id + ".jpg')"
    $(this).children('img').css 'background-image', "url('/assets/images/contents/people/" + this.id + ".png')"
  )
  $('.people_gallery > div').each( () ->
    idReplace = $(this).attr('id').replace(' ', '').replace('&', '')
    $(this).attr 'id', idReplace
  )
  $('.filters .items > div').click () ->
    idToShow = $(this).attr 'id'
    $('.people_gallery > div').removeClass '-on'
    $('.people_gallery > div#' + idToShow).addClass '-on'
  $('.filters #all').click ->
    $('.people_gallery > div').addClass '-on'

#  Contact
  $('.contact #contact_info_btn').click ->
    $('.contact .contact_wrapper').removeClass('-company_view')
    $('.contact .contact_wrapper').addClass('-contact_info')

  $('.contact #company_view_btn').click ->
    $('.contact .contact_wrapper').removeClass('-contact_info')
    $('.contact .contact_wrapper').addClass('-company_view')
    image_process '#view_large'
    for x in [1..6]
      image_process '#thumb_' + x

  $('.contact #company_view li').mouseenter ->
    view_id = $(this).attr 'id'
    company_view_choice view_id.replace('li_', '')

  $('.contact .thumbnails div').mouseenter ->
    view_id = $(this).attr 'id'
    company_view_choice view_id.replace('thumb_', '')

  $('.contact #company_view .thumbnails > div').each () ->
    thumb_id = $(this).attr('id')
    img_id = thumb_id.replace('thumb_', '')
    img_src = '/assets/images/contents/company_view/' + img_id + '.jpg'
    $('#' + thumb_id).css "background-image", "url('/assets/images/contents/company_view/" + img_id + ".jpg')"


# Contact 회사 전경
@company_view_choice = (id) ->
  $('.contact #view_large').css 'background-image', "url('/assets/images/contents/company_view/" + id + ".jpg')"
  $('#company_view ul li').removeClass('-on')
  $('#li_' + id).addClass('-on')
  $('.thumbnails div').removeClass('-on')
  $('#thumb_' + id).addClass('-on')
