#!/usr/bin/env python
# -*- coding: utf-8 -*-

import unittest

__all__ = ['test_models']

def suite():
    """Return TestSuite for all unit-test of pytils"""
    suite = unittest.TestSuite()
    loader = unittest.defaultTestLoader
    for module_name in __all__:
        imported_module = __import__("dict.tests."+module_name,
                                       globals(),
                                       locals(),
                                       ["dict.tests"])
        
        suite.addTest(loader.loadTestsFromModule(imported_module))
#        suite.addTest(get_django_suite())


    return suite

if __name__ == '__main__':
    unittest.main(suite())
