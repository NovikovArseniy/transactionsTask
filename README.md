# Задание для стажировки
Описание методов контроллера.  
  
POST запрос по адресу /order/transfer c телом  {  
                                                "fromAccount": "\*String fromAccount id\*",  
                                                "toAccount": "\*String toAccount id\*",  
                                                "amount": \*double amount\*  
                                              }  - Создать заявку на перевод.  
                                                
GET запрос по адресу /order/history - Показать историю транзакций.  
  
POST запрос по адресу /account/create c телом  {  
                                                "id": "\*String id\*",  
                                                "amount": \*double amount\*  
                                              }  - Создать аккаунт с данным id и amount, количеством средств. Так как данные о счетах хранятся в памяти, метод необходим для тестов.  
                                                
GET запрос по адресу /account/get c параметром  {  
                                                "id": "\*String id\*"  
                                              }  - Получить данные об аккаунте по id. Необходимо для тестов.  
                                                
GET запрос по адресу /account/all - Получить данные обо всех аккаунтах. Необходимо для тестов.  
    
Описание тестов.  
  
createSingleAccount - тест создания аккаунта.  
createAccountWithNegativeAmount - тест создания аккаунта с отрицательным балансом.  
createManyAccounts - тест создания множества аккаунтов.  
createSingleTransaction - тест транзакции.  
createNegativeTransaction - тест транзакции с отрицательной суммой перевода.  
createInvalidTransaction - тест транзакции с суммой перевода большей, чем доступно средств.  
createManyTransactions - тест проведения большого количества параллельных транзакций.  
  
Дополнение.  
transactions-0.1.0.jar - исполняемый jar-файл. Запуск на порте 8080.
