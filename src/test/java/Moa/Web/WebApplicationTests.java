package Moa.Web;

import Moa.Web.DTO.TokenInfo;
import Moa.Web.Entity.Member;
import Moa.Web.Entity.Tag;
import Moa.Web.Repository.MemberRepository;
import Moa.Web.Repository.MembertagRepository;
import Moa.Web.Repository.TagRepository;
import Moa.Web.Service.MemberService;
import Moa.Web.Service.TagService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class WebApplicationTests {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	MemberService memberService;

	@Autowired
	TagRepository tagRepository;

	@Autowired
	MembertagRepository membertagRepository;

	@Autowired
	TagService tagService;

	@Test
	void regenerateTokenTest() throws Exception{

	}


	@Test
	@Rollback(value = false)
	void joinServiceTest() {
		Tag tag = Tag.builder()
				.id(1L)
				.tname("학사")
				.build();
		Member member = Member.builder()
				.id(1L)
				.address("voiceofwon@naver.com")
				.name("김원중")
				.password("won1102")
				.S_id(202126978)
				.roles(List.of("USER"))
				.membertags(List.of())
				.build();

		memberService.join(member);
		tagService.insertTag(tag);



	}

	@Test
	@Transactional
	void InsertTagToMemberTest(){
		boolean app = memberService.addFormalTag(memberRepository.findByName("김원중"), tagRepository.findByTname("학사").get());

		Assertions.assertEquals(app, true);

	}

}


@ExtendWith(MockitoExtension.class)
class ServiceUnitTest{

	@Mock
	MemberRepository memberRepository;

	@Mock
	MembertagRepository membertagRepository;
	@Mock
	TagRepository tagRepository;
	@InjectMocks
	MemberService memberService;

	@Test
	void InsertTagToMemberTest(){
		BDDMockito.given(memberRepository.findByName("김원중")).willReturn(Member.builder()
						.address("voiceofwon@naver.com")
						.id(1L)
						.membertags(List.of())
						.name("김원중")
						.password("won1102")
						.S_id(202126978)
						.roles(List.of("USER"))
						.build());
		BDDMockito.given(tagRepository.findByTname("학사")).willReturn(Optional.of(Tag.builder()
				.id(1L)
				.tname("학사")
				.build()));


		Member member = memberRepository.findByName("김원중");
		Tag tag = tagRepository.findByTname("학사").get();

		boolean app = memberService.addFormalTag(member, tag);

		Assertions.assertEquals(app,true);

	}

	@Test
	void regenerateTokenTest(){
	}

}
