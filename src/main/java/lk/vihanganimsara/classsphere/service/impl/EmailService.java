package lk.vihanganimsara.classsphere.service.impl;

import lk.vihanganimsara.classsphere.entity.AttendanceStatus;
import lk.vihanganimsara.classsphere.entity.CourseSession;
import lk.vihanganimsara.classsphere.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendAttendanceEmails(Student student, CourseSession session, AttendanceStatus status) {
        String subject = "Attendance Recorded - " + session.getCourse().getTitle();

        // Student mail
        String studentMsg = "Hi " + student.getName() +
                ",\n\nYour attendance has been marked for " +
                session.getCourse().getTitle() + " on " + session.getSessionDate() +
                " at " + session.getStartTime() + ".\n\nRegards,\nABC Institute";
        sendEmail(student.getEmail(), subject, studentMsg);

        // Guardian mail
        if (student.getGuardianEmail() != null) {
            if (status.equals(AttendanceStatus.LATE)){
                String guardianMsg = "Dear Parent,\n\nThis is to notify you that " +
                        student.getName() + " attended " + session.getCourse().getTitle() +
                        " on " + session.getSessionDate() + " at " + session.getStartTime() +"(HE IS LATE)"+
                        ".\n\nRegards,\nABC Institute";
                sendEmail(student.getGuardianEmail(), subject, guardianMsg);

            } else{
                String guardianMsg = "Dear Parent,\n\nThis is to notify you that " +
                        student.getName() + " attended " + session.getCourse().getTitle() +
                        " on " + session.getSessionDate() + " at " + session.getStartTime() +
                        ".\n\nRegards,\nABC Institute";
                sendEmail(student.getGuardianEmail(), subject, guardianMsg);
            }
        }
    }

    private void sendEmail(String to, String subject, String body) {
        if (to == null || to.isEmpty()) return;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
