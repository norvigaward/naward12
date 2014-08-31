#!/usr/bin/env python
class mymiddleware:
    def process_response(self,request,response,spider):
        log.msg("In Middleware " + response.url, level=log.INFO)

