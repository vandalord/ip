public class Jax {

    public static void greet() {
        // Start-up message text
        String greeting = " \uD83D\uDC4B你好! I'm Jax, your personal assistant chatbot!'\n"
                        + " What can I do for you?";
        System.out.println(greeting);
        separate_text();
    }

    public static void exit() {
        // Exit message text
        String goodbyes = " \uD83D\uDC4B再见. Hope to see you again soon!";
        separate_text();
        System.out.println(goodbyes);
    }

    public static void separate_text() {
        String line = "―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――";
        System.out.println(line);
    }

    public static void main(String[] args) {
        greet();
        await_input();
        exit();
    }
}
