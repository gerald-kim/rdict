
import pywiktionary
import settings

class PywiktionaryMiddleware(object):
    """
    """
    def process_request(self, request):
        """Opens pywiktionary dictionary"""
        request.pywiktionary = pywiktionary.Dictionary(settings.DICT_DB)
        #request.pywiktionary.open()
        
    def process_exception(self, request, exception):
        """Closes pywkitionary dictionary"""
        self._close_pywiktionary(request)

    def process_response(self, request, response):
        """Closes pywkitionary dictionary"""
        self._close_pywiktionary(request)
        return response
            
    def _close_pywiktionary(self, request):
        #request.pywiktionary.close()
        #request.pywiktionary = None
        pass
                
