Rabbit MQ - Testes com Java 


Simulador: https://tryrabbitmq.com/

Producer: Aplicativo que envia as mensagens.
Consumer: Aplicativo que recebe as mensagens.
Queue: Buffer que armazena mensagens.
Message: Informação que é enviada do produtor para um consumidor através do RabbitMQ.
Connection: Uma conexão é uma conexão TCP entre seu aplicativo e o broker RabbitMQ.
Channel: Um canal é uma conexão virtual dentro de uma conexão. Quando você está publicando ou consumindo mensagens de uma fila, tudo é feito em um canal.
Exchange: Recebe mensagens dos produtores e as empurra para filas dependendo das regras definidas pelo tipo de troca. Para receber mensagens, uma fila precisa estar vinculada a pelo menos uma troca.
Binding: Um binding é um link entre uma fila e uma troca.
Routing key: a chave de roteamento é uma chave que a troca analisa para decidir como rotear a mensagem para as filas. A chave de roteamento é como um endereço para a mensagem.


O Producer envia/publica as mensagens para o broker -> Os consumers recebem as mensagens do broker. O RabbitMQ atua como um middleware de comunicação entre produtores e consumidores, mesmo que sejam executados em máquinas diferentes.
O componente Exchange no RabbitMQ é responsável por rotear as mensagens para diferentes filas. O Exchange usa a chave de roteamento para rotear as mensagens para as respectivas filas.
![image](https://github.com/cassioseffrin/RabbitMQTests/assets/13109831/0b65a097-b083-406a-b058-2113c81dfbac)


Enviar mensagem para várias filas
Por ter uma aplicação mais complexa teríamos várias filas. Portanto, as mensagens serão enviadas em várias filas.
![image](https://github.com/cassioseffrin/RabbitMQTests/assets/13109831/1ab44e06-fd47-43c9-964a-7fc856067374)



exemplo android:
https://github.com/cloudamqp/android-example/tree/main

