CREATE TABLE public."User"
(
    firstname character varying(50) NOT NULL,
    lastname character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    gender character varying(10) NOT NULL,
    birthdate date NOT NULL,
    profile_image bytea,
    age integer,
    password character varying(255) NOT NULL,
    CONSTRAINT "User_pkey" PRIMARY KEY (email),
    CONSTRAINT "User_email_check" CHECK (email::text ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'::text)
);


CREATE TABLE teacher
(
    salary numeric,
    email TEXT PRIMARY KEY ,
	CONSTRAINT fk_email_teacher FOREIGN KEY (email)
        REFERENCES public."User" (email) ON DELETE CASCADE
        ON UPDATE CASCADE
   );


CREATE TABLE public.student
(
       email TEXT PRIMARY KEY ,
	   CONSTRAINT fk_email_student FOREIGN KEY (email)
        REFERENCES public."User" (email) ON DELETE CASCADE
        ON UPDATE CASCADE
 );


CREATE TABLE public.manager
(
	email text,
    
    CONSTRAINT email_manager PRIMARY KEY (email),
   CONSTRAINT fk_email_manager FOREIGN KEY (email)
 REFERENCES public."User" (email) ON DELETE CASCADE
ON UPDATE CASCADE
);


CREATE TABLE public.course
(
    course_id serial NOT NULL ,
    name_course character varying  NOT NULL,
    start_time time without time zone NOT NULL,
    end_time time without time zone NOT NULL,
    photo bytea,
    max_capacity integer NOT NULL,
    email_teacher text ,
    CONSTRAINT "Course_pkey" PRIMARY KEY (course_id),
    CONSTRAINT fk_teacher FOREIGN KEY (email_teacher) REFERENCES public.teacher (email) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE public.slide
(
    course_id integer NOT NULL,
    slide_title character varying default ' ',
    slide_link text NOT NULL,
    CONSTRAINT slide_pkey PRIMARY KEY (course_id,slide_title,slide_link),
    CONSTRAINT slide_course_id_fkey FOREIGN KEY (course_id)
        REFERENCES public.course (course_id) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE public.link
(
    course_id integer NOT NULL,
    link_title character varying default ' ',
    link_url text not NULL,
    CONSTRAINT link_pkey PRIMARY KEY (course_id,link_title,link_url),
    CONSTRAINT link_course_id_fkey FOREIGN KEY (course_id)
        REFERENCES public.course (course_id) ON DELETE CASCADE
ON UPDATE CASCADE
);


CREATE TABLE public.study
(
    course_id integer NOT NULL,
    student_email text  NOT NULL,
    CONSTRAINT study_pkey PRIMARY KEY (course_id, student_email),
    CONSTRAINT study_course_id_fkey FOREIGN KEY (course_id)
        REFERENCES public.course (course_id) ON DELETE CASCADE
ON UPDATE CASCADE,
    CONSTRAINT study_student_email_fkey FOREIGN KEY (student_email)
        REFERENCES public.student (email) ON DELETE CASCADE
ON UPDATE CASCADE
);


CREATE TABLE public.lecture
(
    course_id integer NOT NULL,
    lecture_date date NOT NULL,
    CONSTRAINT lecture_pkey PRIMARY KEY (course_id, lecture_date),
    CONSTRAINT lecture_course_id_fkey FOREIGN KEY (course_id)
        REFERENCES public.course (course_id) ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE public.attendance
(
    student_email text COLLATE pg_catalog."default" NOT NULL,
    course_id integer NOT NULL,
    lecture_date date NOT NULL,
    attendance_status character varying,
    CONSTRAINT attendance_pkey PRIMARY KEY (student_email, course_id, lecture_date),
    CONSTRAINT attendance_course_id_lecture_id_fkey FOREIGN KEY (course_id, lecture_date)
        REFERENCES public.lecture (course_id, lecture_date)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT attendance_student_email_fkey FOREIGN KEY (student_email,course_id)
        REFERENCES public.study (student_email,course_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);


create table public.banned(
course_id integer NOT NULL,
    student_email text  NOT NULL,
    CONSTRAINT banned_pkey PRIMARY KEY (course_id, student_email),
    CONSTRAINT banned_course_id_fkey FOREIGN KEY (course_id)
        REFERENCES public.course (course_id) ON DELETE CASCADE
ON UPDATE CASCADE,
    CONSTRAINT banned_student_email_fkey FOREIGN KEY (student_email)
        REFERENCES public.student (email) ON DELETE CASCADE
ON UPDATE CASCADE
);


create table public."message"(
message_id serial primary Key,
message_content text,
);


create table public.allmessages(
message_id integer ,
email text ,
primary key (message_id,email),
foreign key(message_id) references public.message(message_id) ON DELETE CASCADE
ON UPDATE CASCADE,
foreign key (email) references public."User"(email) ON DELETE CASCADE
ON UPDATE CASCADE
);


CREATE VIEW public.absences AS
SELECT 
    s.course_id,
    s.student_email,
    COUNT(*) FILTER (WHERE a.attendance_status = 'Absent' or a.attendance_status = 'absent' ) AS absence_count
FROM public.study s
LEFT JOIN  public.attendance a
    ON s.course_id = a.course_id
    AND s.student_email = a.student_email
GROUP BY s.course_id, s.student_email;


alter table public.course add column notic text default 'Empty';
alter table public.message add column "date" date default current_date;
alter table public."User" add column user_type integer;
SET datestyle = 'DMY';


CREATE OR REPLACE FUNCTION update_age()
RETURNS TRIGGER AS $$
BEGIN
    NEW.age := DATE_PART('year', AGE(current_date, NEW.birthdate));
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_update_age
BEFORE INSERT OR UPDATE OF birthdate ON public."User"
FOR EACH ROW
EXECUTE FUNCTION update_age();


INSERT INTO public."User" 
(firstname, lastname, email, gender, birthdate, profile_image, age, password,user_type)
VALUES
('Lina', 'Mohammed', 'lina.khallid@gmail.com', 'Female', '2006-07-04', NULL, 19, '369852147',1),
('Ali', 'Mahmoud', 'ali.mahmoud@gmail.com', 'Male', '1990-03-20', NULL, 35, 'hashed_password_1',1),
('Hadi', 'Yousef', 'hadi.yousef@gmail.com', 'Female', '2013-08-14', NULL, 12, '21174632',1),
('Omar', 'Saleh', 'omarsaleh@gmail.com', 'Male', '1995-08-14', NULL, 30, '25874136',2),
('Ahmad', 'Hassan', 'ahmad123@gmail.com', 'Male', '1980-06-15', NULL, 45, '987632541',3),
('Hadi', 'Yousef', 'hadiyousef@gmail.com', 'Male', '1999-08-14', NULL, 26, '21174632',2),
('Ali', 'Ahmad', 'ali.teacher@gmail.com', 'Male', '1980-02-19', NULL, 45, '12457896',2),
('raya', 'abd', 'raya@gmail.com', 'Female', '1995-01-19', NULL, 30, '1254896',2),
('Omar', 'Ahmad', 'omarh@gmail.com', 'Male', '2007-10-05', NULL, 17, '25874136',2),
('Omar', 'Saleh', 'omar.saleh@gmail.com', 'Male', '2007-10-05', NULL, 17, '25874136',1),
('Yousef', 'Tarek', 'yousef.tarek@gmail.com', 'Male', '1985-01-05', NULL, 40, 'hashed_password_5',1);

INSERT INTO public.teacher (salary, email)
VALUES
(1000, 'omarsaleh@gmail.com'),
(10000, 'hadiyousef@gmail.com'),
(20000, 'raya@gmail.com'),
(2500, 'ali.teacher@gmail.com'),
(2000, 'omarh@gmail.com');

INSERT INTO public.student (email)
VALUES
('omar.saleh@gmail.com'),
('lina.khalil@gmail.com'),
('hadi.yousef@gmail.com'),
('ali.mahmoud@example.com'),
('yousef.tarek@example.com');

INSERT INTO public.course (name_course, start_time, end_time, photo, max_capacity, email_teacher) VALUES
('java', '08:00', '10:00', NULL, 30, 'linaHamdan@gmail.com'),
('C', '10:30', '12:30', NULL, 25, 'sara.teacher@gmail.com'),
('C++', '13:00', '15:00', NULL, 20, 'sara.teacher@gmail.com'),
('OOP', '15:30', '17:30', NULL, 20, 'mohammed.saleh@gmail.com'),
('Python', '09:00', '11:00', NULL, 35, 'hassan@gmail.com');

INSERT INTO public.study (course_id, student_email) VALUES
(1, 'lina.khalil@gmail.com'),
(5, 'samir.nasser@gmail.com'),
(3, 'hadi.yousef@gmail.com'),
(4, 'samir.nasser@gmail.com'),
(2, 'omar.saleh@gmail.com');

INSERT INTO public.banned (course_id, student_email) VALUES
(1, 'lina.khallid@gmail.com'),
(1, 'ali.mahmoud@gmail.com'),
(3, 'hadi.yousef@gmail.com'),
(5, 'omarsaleh@gmail.com'),
(5, 'ahmad123@gmail.com');


INSERT INTO public.slide (course_id, slide_title, slide_link) VALUES
(1, 'CH01 Introduction', 'file:///C:/Users/Hp/eclipse-workspace/myfx/src/application/Ch01_Introduction.pdf'),
(1, 'CH02 Introduction to GUI', 'file:///C:/Users/Hp/eclipse-workspace/myfx/src/application/Ch02_IntroductiontoGUI.pdf'),
(1, 'CH03 Exceptions', 'file:///C:/Users/Hp/eclipse-workspace/myfx/src/application/Ch03_Exception.pdf'),
(4, 'CH01', 'file:///C:/Users/ASUS/Downloads/Ch%2008%20-%20Exception.pdf'),
(5, 'CH01', 'file:///C:/Users/ASUS/Downloads/Ch%2002%20-%20Introduction%20to%20GUI.pdf');

INSERT INTO public.link  VALUES
(1, 'Zoom Link', 'https://najah.zoom.us/j/3022109094?pwd=MjB6cVBYdVNMMHZqN1NJcE5KYXEvQT09#success'),
(1, 'Java Online Compiler', 'https://www.programiz.com/java-programming/online-compiler/'),
(1, 'Course Book', 'https://algs4.cs.princeton.edu/home/'),
(2, 'Zoom Link', 'https://najah.zoom.us/j/93917726141?pwd=F0XWV9uP8NNabvRsd9Mrhzec6ESh6a.1'),
(2, 'C Online Compiler', 'https://www.programiz.com/c-programming/online-compiler/'),
(2, 'Course Book', 'https://seriouscomputerist.atariverse.com/media/pdf/book/C%20Programming%20Language%20-%202nd%20Edition%20(OCR).pdf'),
(3, 'Zoom Link', 'https://najah.zoom.us/j/93917726141?pwd=F0XWV9uP8NNabvRsd9Mrhzec6ESh6a.1'),
(3, 'C++ Online Compiler', 'https://www.programiz.com/cpp-programming/online-compiler/'),
(3, 'Course Book', 'https://www.idpoisson.fr/volkov/C++.pdf'),
(4, 'Zoom Link', 'https://najah.zoom.us/j/93917726141?pwd=F0XWV9uP8NNabvRsd9Mrhzec6ESh6a.1'),
(4, 'Course Book', 'https://engineering.futureuniversity.com/BOOKS%20FOR%20IT/OOP.pdf'),
(5, 'Zoom Link', 'https://najah.zoom.us/j/3022109094?pwd=MjB6cVBYdVNMMHZqN1NJcE5KYXEvQT09#success'),
(5, 'Python Online Compiler', 'https://www.programiz.com/python-programming/online-compiler/'),
(5, 'Course Book', 'https://assets.openstax.org/oscms-prodcms/media/documents/Introduction_to_Python_Programming_-_WEB.pdf');

UPDATE public.course 
SET notic = 'New slides have been uploaded to the course materials section.' 
WHERE course_id = 1;

UPDATE public.course 
SET notic = 'The recording of the last lecture is now available.' 
WHERE course_id = 2;

UPDATE public.course 
SET notic = 'Additional reference links have been added for further reading.' 
WHERE course_id = 3;

UPDATE public.course 
SET notic = 'Updated timetable is now live on the course page.' 
WHERE course_id = 4;

UPDATE public.course 
SET notic = 'A new discussion thread has been created for this week’s topic.' 
WHERE course_id = 5;

update public."User" set user_type =1 where email='omar.saleh@gmail.com';
update public."User" set user_type =1 where email='lina.khalil@gmail.com';
update public."User" set user_type =1 where email='hadi.yousef@gmail.com';
update public."User" set user_type =1 where email='maya.hamdan@gmail.com';
update public."User" set user_type =1 where email='samir.nasser@gmail.com';

update public."User" set user_type =2 where email='ali.teacher@gmail.com';
update public."User" set user_type =2 where email='sara.teacher@gmail.com';
update public."User" set user_type =2 where email='mohammed.saleh@gmail.com';
update public."User" set user_type =2 where email='linaHamdan@gmail.com';
update public."User" set user_type =2 where email='hassan@gmail.com';

INSERT INTO public.manager (email)
VALUES
('ahmad123@gmail.com');
update public."User" set user_type =3 where email='ahmad123@gmail.com';

insert into public.lecture values
(1,'2025-09-02'),
(1,'2025-09-03'),
(1,'2025-09-04'),
(1,'2025-08-20'),
(1,'2025-08-21'),
(1,'2025-08-23'),
(1,'2025-08-24'),
(1,'2025-08-25'),
(1,'2025-08-26'),
(2,'2025-09-02'),
(2,'2025-09-03'),
(2,'2025-09-04'),
(2,'2025-08-20'),
(2,'2025-08-21'),
(2,'2025-08-22'),
(2,'2025-08-23'),
(2,'2025-08-24'),
(2,'2025-08-25'),
(2,'2025-08-26'),
(3,'2025-08-14'),
(3,'2025-08-15'),
(3,'2025-08-16'),
(3,'2025-08-17'),
(3,'2025-08-19'),
(4,'2025-08-14'),
(4,'2025-08-15'),
(4,'2025-08-16'),
(4,'2025-08-17'),
(4,'2025-08-19'),
(5,'2025-08-23'),
(5,'2025-08-24'),
(5,'2025-08-25'),
(5,'2025-08-26');

insert into public.attendance values
('omar.saleh@gmail.com',1,'2025-09-02','absent'),
('omar.saleh@gmail.com',1,'2025-09-03','present'),
('omar.saleh@gmail.com',1,'2025-09-04','present'),
('omar.saleh@gmail.com',1,'2025-08-21','present'),
('omar.saleh@gmail.com',1,'2025-08-24','present'),
('omar.saleh@gmail.com',1,'2025-08-25','absent'),
('omar.saleh@gmail.com',1,'2025-08-26','present'),
('omar.saleh@gmail.com',2,'2025-09-02','present'),
('omar.saleh@gmail.com',2,'2025-09-04','absent'),
('omar.saleh@gmail.com',2,'2025-08-24','present'),
('omar.saleh@gmail.com',2,'2025-08-25','present'),
('omar.saleh@gmail.com',2,'2025-08-26','present'),
('omar.saleh@gmail.com',3,'2025-08-14','present'),
('omar.saleh@gmail.com',3,'2025-08-15','absent'),
 
INSERT INTO public."message" (message_id,message_content)VALUES
(1,'A new course has been added: "Intro to Java". Please check the platform for details.'),
(2,'If you exceed 5 absences in a course, you will be disqualified from completing it.'),
(3,'Please ensure you attend all lectures and keep track of your attendance on the platform.'),
(4,'Please schedule your upcoming lectures for the next month.'),
(5,'Please upload the required learning materials for your courses before the start of next week.');

insert into public.allmessages values
(1,lina.khalil@gmail.com),
(1,samir.nasser@gmail.com),
(1,ali.teacher@gmail.com),
(2,mohammed.saleh@gmail.com),
(2,omar.saleh@gmail.com),
(3,lina.khalil@gmail.com);





