package pcd.assignment3;

public class Protocol {

    public static class Msg{
        final String content;

        public Msg(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }

}
