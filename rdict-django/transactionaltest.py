
import unittest
from django.db import transaction

class TransactionalTestCase(unittest.TestCase):
    def setUp(self):
        super(TransactionalTestCase, self).setUp()
        
        transaction.enter_transaction_management()
        transaction.managed(True)
        
        self.setUpInTransaction()
        
    def tearDown(self):
        super(TransactionalTestCase, self).tearDown()

        if transaction.is_dirty():
            transaction.rollback()
        transaction.leave_transaction_management()
        
    def setUpInTransaction(self):
        pass