Rabbit MQ - Testes com Java 


Simulador: https://tryrabbitmq.com/

<b>Producer:</b> Aplicativo que envia as mensagens.
<b>Consumer:</b> Aplicativo que recebe as mensagens.
<b>Queue:</b> Buffer que armazena mensagens.
<b>Message:</b> Informação que é enviada do produtor para um consumidor através do RabbitMQ.
<b>Connection:</b> Uma conexão é uma conexão TCP entre seu aplicativo e o broker RabbitMQ.
<b>Channel:</b> Um canal é uma conexão virtual dentro de uma conexão. Quando você está publicando ou consumindo mensagens de uma fila, tudo é feito em um canal.
<b>Exchange:</b> Recebe mensagens dos produtores e as empurra para filas dependendo das regras definidas pelo tipo de troca. Para receber mensagens, uma fila precisa estar vinculada a pelo menos uma troca.
<b>Binding:</b> Um binding é um link entre uma fila e uma troca.
<b>Routing key:</b> a chave de roteamento é uma chave que a troca analisa para decidir como rotear a mensagem para as filas. A chave de roteamento é como um endereço para a mensagem.


O Producer envia/publica as mensagens para o broker -> Os consumers recebem as mensagens do broker. O RabbitMQ atua como um middleware de comunicação entre produtores e consumidores, mesmo que sejam executados em máquinas diferentes.

O componente Exchange no RabbitMQ é responsável por rotear as mensagens para diferentes filas. O Exchange usa a chave de roteamento para rotear as mensagens para as respectivas filas.
![image](https://github.com/cassioseffrin/RabbitMQTests/assets/13109831/0b65a097-b083-406a-b058-2113c81dfbac)

Exemplo de uma aplicação mais complexa com várias filas:
![image](https://github.com/cassioseffrin/RabbitMQTests/assets/13109831/1ab44e06-fd47-43c9-964a-7fc856067374)

Tipos de exchange
O protocolo AMQP na versão 0-9-1 fornece cinco tipos de exchange:

1. Default exchange;
2. Direct exchange;
3. Fanout exchange;
4. Topic exchange;
5. Headers exchange;
 

<b>Default exchange</b>
Esse é o exchange padrão fornecida pelo broker e, não possui nome. Apesar disso, tem uma característica muito interessante: toda fila criada é automaticamente associada a ela com uma rota que é igual ao nome da fila. Em miúdos, se eu declarar uma fila com o nome foo e publicar uma mensagem no default exchange com a rota foo esta mensagem será entregue na fila de mesmo nome, dando a impressão que estou me comunicando diretamente com a fila.

<b>Direct exchange</b>
Diferente do exchange apresentado anteriormente, este possui nome e seu comportamento é de encaminhar mensagens que possuam exatamente a mesma rota das filas associadas a este exchange.

Exemplificando:

Uma fila se associa a um exchange com a rota foo. Quando uma nova mensagem com a rota foo chega no direct exchange ele a encaminha para a fila foo.

O direct exchange é uma ótima opção para trabalhar com diversos consumers de maneira balanceada já que o algoritmo utilizado pelo RabbitMQ para distribuição de mensagens entre consumers é o round-robin.

<b>Fanout exchange</b>
Este exchange simplesmente ignora a rota. Seu comportamento resume-se em mandar uma cópia das mensagens para todas as filas que estão associadas a ele.

Um bom exemplo de uso para este tipo de exchange é fazer o broadcast de uma mudança de configuração para n filas, fazendo com que seus respectivos consumers repliquem as alterações.

<b>Topic exchange</b>
Este exchange encaminha mensagens de acordo com a rota definida na mensagem e o padrão definido na associacão da fila ao exchange. Por exemplo, vamos supor que eu queira agregar logs do Apache. Eu defini que vou ter filas distintas para acesso e erros e uma fila que vai receber os dois tipos.

Os bindings seriam os seguintes:

Q1 (logs de acesso): apache.access;
Q2 (logs de erro): apache.error;
Q3 (logs de erro): apache.*;
Então, se meu producer mandar uma mensagem com rota apache.access tanto a fila Q1 quanto a fila Q3 receberão as mensagens porque o padrão (como se fosse uma regex) bate. O mesmo vale se meu producer enviar uma mensagem com a rota apache.error, a diferença é que a fila Q2 receberá mensagem ao invés da fila Q1.

É muito importante ressaltar que os padrões tem duas regras básicas o * substitui apenas UMA palavra. Se você quiser substituir zero ou mais (assim como o operador * da regex) você precisa usar o #.

<b>Headers exchange</b>
Esse é o tipo menos comum de exchange. Este tipo ignora a rota e encaminha as mensagens usando seu cabeçalho. Então se você possui uma condição mais complexa que uma string para encaminhar a mensagem você pode fazer uso deste tipo de exchange. Ele funciona BEM mais ou menos como um if, já que é possível definir mais de um critério para encaminhamento da mensagem, e ainda possibilita o uso de hashes ou inteiros como “rotas” se houver a necessidade de usar algo mais complexo.



file:///Users/programacao/dev/MensageiroMQ/src/main/resources/architecture.png![image](https://github.com/cassioseffrin/RabbitMQTests/assets/13109831/d62877c4-179a-4d08-8e75-9e04d2f79c1e)


exemplo no android:
https://github.com/cloudamqp/android-example/tree/main

