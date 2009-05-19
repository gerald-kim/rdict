from django import template
from django.template.defaultfilters import stringfilter

import re
register = template.Library()

@register.filter
@stringfilter
def format_dictionary( string ):
    definition_re = re.compile(r'^\s+(\d+.)([^\n]*)$', re.MULTILINE)
    
    string = definition_re.sub('<span class="definition"><b>\g<1></b>\g<2></span>', string)
    
    string = re.sub(r'\n', '<br>\n', string)
     
    return string
